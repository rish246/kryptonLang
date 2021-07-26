#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct testcase_ {
    const char *right_ans;
    const char *given_ans;
} testcase;

testcase *generate_testcases(const char *given_answers[], const char *right_answers[], int n_cases) {
    testcase* our_testcases = (testcase *) malloc(n_cases * sizeof(testcase));

    for(int i=0; i<n_cases; i=i+1) {
        const char *given_ans = given_answers[i];
        const char *right_ans = right_answers[i];
        testcase new_testcase;
        new_testcase.given_ans = given_ans;
        new_testcase.right_ans = right_ans;
        our_testcases[i] = new_testcase;
    }

    return our_testcases;
} 

int count_score(testcase* testcases, int n_cases) {
    int total_score = 0;
    for(int i=0; i<n_cases; i++) {
        int is_match = strcmp(testcases[i].given_ans, testcases[i].right_ans);
        total_score += (is_match == 0) ? 0 : 1;
    }
    return total_score;
}

int main() {
    int n_cases = 5;
    const char *given_answers[] = {"Rishabh", "katna", "yes", "no", "true"};
    const char *right_answers[] = {"Rishabh", "Katna", "no", "no", "false"};

    testcase* our_testcases = generate_testcases(given_answers, right_answers, n_cases);
    // int scores = count_score(our_testcases, n_cases);
    int n_cases_passed = count_score(our_testcases, n_cases);
    for(int i=0; i<n_cases; i++) {
        printf("%s -> %s\n", our_testcases[i].given_ans, our_testcases[i].right_ans);
    }

    printf("%d cases passed of %d\n", n_cases_passed, n_cases);
    return 0;
}