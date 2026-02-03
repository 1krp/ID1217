/* matrix summation using pthreads

   features: uses a barrier; the Worker[0] computes
             the total sum from partial sums computed by Workers
             and prints the total sum to the standard output

   usage under Linux:
     gcc matrixSum.c -lpthread
     a.out size numWorkers

*/
#include <omp.h>
#include <limits.h>
double start_time, end_time;
#include <stdlib.h>
#include <stdio.h>
#define MAXLOOP 5
#define SPLITTHRESHOLDMAX 10000

int size; 
int loop;
int splitterThreshold;
int taskCount;
double start_time, end_time; /* start and end times */
int* array;

void quickSort(int *org, int start, int end);
int partition(int *org, int start, int end);
int* randomArray(int size);

void quickSort(int *org, int start, int end) {
    if (end <= start) {
        return;
    }                   //base case
    int pivot = partition(org, start, end);

    #pragma omp task if (end - start > splitterThreshold) 
    quickSort(org, start, pivot - 1);

    #pragma omp task if (end - start > splitterThreshold)
    quickSort(org, pivot + 1, end);

    #pragma omp taskwait
}

int partition(int *org, int start, int end) {
    int pivot = org[end];
    int i = start -1;
    
    for (int j = start; j <= end -1; j++) {
        if(org[j] < pivot) {
            i++;
            int temp = org[i];
            org[i] = org[j];
            org[j] = temp;
        }
    }
    i++;
    int temp = org[i];
    org[i] = org[end];
    org[end] = temp;
    return i;
}

int* randomArray(int size) {
    int* randomArray = malloc(size * sizeof(int));

    for (int i = 0; i < size; i++) {
       randomArray[i] = rand()%(2*size);
    }
    return randomArray; 
}
void printArray(int arr[], int size) {
    printf("[ ");
    for (int i = 0; i < size; i++) {
        printf("%d ", arr[i]);
    }
    printf("]\n");
}

int main(int argc, char *argv[]) {
    int i, j;
    int numberOfThreads[] = {1,2,4,8,16}; 
    omp_set_dynamic(0);     // Explicitly disable dynamic teams

    loop = (argc > 1)? atoi(argv[1]) : MAXLOOP;
    if (loop > MAXLOOP) loop = MAXLOOP;

    splitterThreshold = (argc > 1)? atoi(argv[2]) : SPLITTHRESHOLDMAX;
    if (splitterThreshold > SPLITTHRESHOLDMAX) splitterThreshold = SPLITTHRESHOLDMAX;

    int arraySize[] = {1000000,2000000,3000000,4000000,5000000,6000000,7000000,8000000,9000000,10000000};

    for(int l = 0; l < loop; l++) {
        for (int a = 0; a < 5; a++) { //threads
            for (int b = 0; b < 10; b++) { //sizes

                omp_set_num_threads(numberOfThreads[a]);
                size = arraySize[b];

                #ifdef DEBUG
                    size = arraySize[b] / 1000000;
                #endif

                int * unsortedArr = randomArray(size);

                #ifdef DEBUG
                    printArray(unsortedArr, size);
                #endif

                start_time = omp_get_wtime();
                #pragma omp parallel  
                {
                    #pragma omp single 
                    {
                        quickSort(unsortedArr, 0, size-1);
                    }
                }
                end_time = omp_get_wtime();


                #ifdef DEBUG
                    printArray(unsortedArr, size);
                #endif

                /* RESULTS */

                #ifdef DEBUG
                    printf(" Size: %d, Threads: %d, Time: %g tasks: %d\n", size, numberOfThreads[a], end_time - start_time, taskCount);
                    printf("\n");
                #else
                    printf("%d, %d, %g\n", size, numberOfThreads[a], end_time - start_time);
                #endif
            }
        } 
    }
}
