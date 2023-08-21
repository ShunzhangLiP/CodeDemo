/**
 *
 * mysh.c
 *
 * CS537 P3
 *
 * Copyright 2021 Shunzhang Li
 *
 */

#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include "myDll.h"

Dnode* dll;

void remove_space(char* src, char* out) {
    int d = 0;
    fflush(stdout);
    for (int s = 0; src[s] != '\0'; s++) {
        if (src[s] != ' ') {
            out[d] = src[s];
            d++;
        }
    out[d] = '\0';
    }
}

static void read_line(const char *userinput) {
    if (strlen(userinput) <= 0)
        return;
    char *line_cp = strdup(userinput);
    char *redirect = strstr(line_cp, ">");
    char *r_args[512];
    if (redirect != NULL) {
        int r_count = 0;
        for (int i = 0; i < strlen(line_cp); i++) {
            if (line_cp[i] == '>')
                r_count++;
            if (line_cp[i] == '\n')
                line_cp[i] = '\0';
        }
        int r_arg = 0;
        char *r_token = strtok(line_cp, ">");
        r_args[r_arg] = r_token;
        while (r_token != NULL) {
            r_token = strtok(NULL, ">");
            r_arg++;
            r_args[r_arg] = r_token;
        }
        free(r_token);
        int f_arg = 0;
        if (r_arg > 1) {
            char *f_token = strtok(r_args[1], " ");
            while (f_token != NULL) {
                f_token = strtok(NULL, " ");
                f_arg++;
            }
            free(f_token);
        }
        if (r_count > 1 || r_arg < 2 || f_arg > 1) {
            int rc_size = strlen("Redirection misformatted.\n");
            write(2, "Redirection misformatted.\n", rc_size);
            return;
        }
    line_cp = r_args[0];
    }

    char dil[4] = " \t\n";
    char *token = strtok(line_cp, dil);
    if (token == NULL) {
        return;
    }
    char *alias_value = get_n(dll, token);
    if (alias_value != NULL) {
        free(line_cp);
        line_cp = strdup(alias_value);
        token = strtok(line_cp, dil);
    }
    int count = 0;
    char *args[512];
    args[0] = token;
    if (strcmp(token, "exit") == 0) {
        free(token);
        exit(0);
    }
    while (token != NULL) {
        token = strtok(NULL, dil);
        count++;
        args[count] = token;
    }
    args[count+1] = NULL;
    free(token);
    if (strcmp(args[0], "alias") == 0) {
        if (args[1] == NULL) {
            list_n(dll);
            return;
        } else if (args[1] != NULL && args[2] == NULL) {
            char *a_value = get_n(dll, args[1]);
            if (a_value != NULL) {
                int size = strlen(args[1]) + 1 + strlen(" ");
                size += strlen(a_value);
                char *line = (char *)malloc(size);
                strcpy(line, args[1]);
                strcat(line, " ");
                strcat(line, a_value);
                strcat(line, "\n");
                write(1, line, strlen(line));
                free(line);
            }
            return;
        } else if (strcmp(args[1], "alias") == 0 || strcmp(args[1], "unalias") == 0 || strcmp(args[1], "exit") == 0) {
            int size = strlen("alias: Too dangerous to alias that.\n");
            write(2, "alias: Too dangerous to alias that.\n", size);
            return;
        } else {
            char* i_value = (char*)malloc(512);
            strcpy(i_value, args[2]);
            for (int i = 3; args[i] != NULL; ++i) {
                strcat(i_value, " ");
                strcat(i_value, args[i]);
            }
            strcat(i_value, "\0");
            dll = insert_n(dll, args[1], i_value);
            free(i_value);
            return;
        }
    }
    if (strcmp(args[0], "unalias") == 0) {
        if (args[1] == NULL || args[2] != NULL) {
            int size = strlen("unalias: Incorrect number of arguments.\n");
            write(2, "unalias: Incorrect number of arguments.\n", size);
            return;
        } else {
            dll = remove_n(dll, args[1]);
            return;
        }
    }
    if (strlen(line_cp) > 0) {
        pid_t pid = fork();

        if (pid != -1) {
            if (pid == 0) {
                if (redirect != NULL) {
                    char* f_name = (char *)malloc(strlen(r_args[1]));
                    remove_space(r_args[1], f_name);
                    int file = open(f_name, O_WRONLY | O_CREAT | O_TRUNC, 0777);
                    free(f_name);
                    if (file == -1) {
                        int file_size = strlen(r_args[1]);
                        char *err_file = (char *)malloc(file_size);
                        strcpy(err_file, f_name);
                        file_size += strlen("Cannot write to file ") + 1;
                        file_size += strlen(".\n");
                        char *err_f = (char *)malloc(file_size);
                        strcpy(err_f, "Cannot write to file ");
                        strcat(err_f, err_file);
                        strcat(err_f, ".\n");
                        write(2, err_f, strlen(err_f));
                        free(err_f);
                        free(err_file);
                        return;
                    }
                    dup2(file, 1);
                }
                execv(args[0], args);
                int new_size = strlen(args[0]);
                new_size += strlen(": Command not found.\n") + 1;
                char *err_cmd = (char *)malloc(new_size);
                strcpy(err_cmd, args[0]);
                strcat(err_cmd, ": Command not found.\n");
                write(STDERR_FILENO, err_cmd, strlen(err_cmd));
                free(err_cmd);
                _exit(1);
            } else {
                int status;
                waitpid(pid, &status, 0);
            }
        } else {
            printf("mysh: fork failed\n");
            fflush(stdout);
            }
    }
    free(line_cp);
}

void int_type() {  // for generating prompt
    char* prompt = "mysh> ";
    printf("%s", prompt);
    fflush(stdout);
}

int main(int argc, char* argv[]) {
    char line[512];
    if (argc < 2) {  // interactive mode
        while (1) {
            int_type();
            char* ret = fgets(line, 512, stdin);
            if (ret == NULL)  // End of file
                break;
            read_line(line);
        }
    } else if (argc > 2) {
        char err_msg[50] = "Usage: mysh [batch-file]\n";
        write(STDERR_FILENO, err_msg , strlen(err_msg));
        return 1;
    } else {
        char *file;
        FILE *fp;
        file = argv[1];
        fp = fopen(file, "r");
        if (fp == NULL) {
            char err_f[512] = "Error: Cannot open file ";
            strcat(err_f, file);
            strcat(err_f, ".\n");
            write(STDERR_FILENO, err_f, strlen(err_f));
            exit(1);
        }
        while (fgets(line, 512, fp) != NULL) {
            write(1, line, strlen(line));
            read_line(line);
        }
        fclose(fp);
    }
    freeall_n(dll);
    return 0;
}
