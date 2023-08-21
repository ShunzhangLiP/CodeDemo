/**
 *
 * myDll.c
 *
 * CS537 P3
 *
 * Copyright 2021 Shunzhang Li
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "myDll.h"

Dnode* insert_n(Dnode* head, char* k, char* v) {
    if (k == NULL || v == NULL) {
        int size = strlen("alias: alias insert failed.\n");
        write(2, "alias: alias insert failed.\n", size);
        return NULL;
    }
    Dnode *newNode;
    if (head == NULL) {
        newNode = (Dnode*)malloc(sizeof(Dnode*) * 2 + sizeof(char*) * 2);
        newNode -> key = (char*)malloc(512);
        strcpy(newNode -> key, k);
        newNode -> value = (char*)malloc(512);
        strcpy(newNode -> value, v);
        head = newNode;
        head -> prev = NULL;
        head -> next = NULL;
        return head;
    } else {
        Dnode *curr = head;
        if (strcmp(curr -> key, k) == 0) {
            strcpy(curr -> value, v);
            return head;
        }
        while (curr -> next != NULL) {
            if (strcmp(curr -> key, k) == 0) {
                strcpy(curr -> value, v);
                return head;
            }
            curr = curr -> next;
        }
        newNode = (Dnode*)malloc(sizeof(Dnode*) * 2 + sizeof(char*) * 2);
        newNode -> key = (char*)malloc(512);
        strcpy(newNode -> key, k);
        newNode -> value = (char*)malloc(512);
        strcpy(newNode -> value, v);
        curr -> next = newNode;
        newNode -> prev = curr;
        newNode -> next = NULL;
    }
    return head;
}

Dnode* remove_n(Dnode* head, char* k) {
    if (head == NULL) {
        return head;
    } else {
        Dnode *curr = head;
        if (strcmp(curr -> key, k) == 0) {
            if (curr -> next != NULL) {
                head = curr -> next;
                head -> prev = NULL;
            } else {
                free(curr -> value);
                free(curr -> key);
                head = NULL;
            }
            return head;
        }
        while (curr -> next != NULL) {
            if (strcmp(curr->next->key, k) == 0) {
                Dnode *tmp = (curr -> next) -> next;
                if (curr -> next != NULL) {
                    free((curr -> next) -> key);
                    free((curr -> next) -> value);
                    free(curr -> next);
                }
                if (tmp != NULL) {
                    curr -> next = tmp;
                } else {
                    curr -> next = NULL;
                }
                return head;
            } else {
                curr = curr -> next;
            }
        }
    }
    return head;
}

void list_n(Dnode* head) {
    Dnode *curr = head;
    char *line;
    while (curr != NULL) {
        line = (char *)malloc(BUFSIZ);
        strcpy(line, curr -> key);
        strcat(line, " ");
        strcat(line, curr -> value);
        strcat(line, "\n\0");
        write(1, line, strlen(line));
        free(line);
        if (curr -> next == NULL) {
            return;
        }
        curr = curr -> next;
    }
}

char* get_n(Dnode* head, char* k) {
    Dnode *curr = head;
    while (curr != NULL) {
        if (strcmp(curr -> key, k) == 0) {
            return curr -> value;
        } else if (curr -> next == NULL) {
            break;
        }
        curr = curr -> next;
    }
    return NULL;
}

void freeall_n(Dnode* head) {
    Dnode* curr = head;
    if (curr == NULL) {
        return;
    }
    while (curr -> next != NULL) {
        curr = curr -> next;
    }
    while (curr -> prev != NULL) {
        free(curr -> value);
        free(curr -> key);
        curr = curr -> prev;
        free(curr -> next);
    }
    free(head -> value);
    free(head -> key);
    free(head);
}
