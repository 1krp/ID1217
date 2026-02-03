
#include <omp.h>
#include <limits.h>
double start_time, end_time;
#include <stdlib.h>
#include <stdio.h>
#define MAXSIZE 10000  /* maximum matrix size */
#define MAXWORKERS 16   /* maximum number of workers */
#define MAXLOOP 10
#define SPLITTHRESHOLD 4

int numThreads;
int size; 
int loop;
#ifdef DEBUG
int taskCount;
#endif
double start_time, end_time; /* start and end times */
int size;  /* assume size is multiple of numWorkers */

int* array;
int loop;
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


void quickSort(int *org, int start, int end) {
    if (end <= start) {
        return;
    }                   //base case
    int pivot = partition(org, start, end);

    #pragma omp task if (end - start > SPLITTHRESHOLD) 
    quickSort(org, start, pivot - 1);

    #pragma omp task if (end - start > SPLITTHRESHOLD)
    quickSort(org, pivot + 1, end);

    #pragma omp taskwait
}

/* read command line, initialize, and create threads */
int main(int argc, char *argv[]) {
  int i, j;
  long l; /* use long in case of a 64-bit system */
  /* read command line args if any */
  size = (argc > 1)? atoi(argv[1]) : MAXSIZE;
  numThreads = (argc > 2)? atoi(argv[2]) : MAXWORKERS;
  if (size > MAXSIZE) size = MAXSIZE;
  if (numThreads > MAXWORKERS) numThreads = MAXWORKERS;

  /* initialize the array */
    int *array = malloc(size * sizeof(int));
	  for (i = 0; i < size; i++) {
          array[i] = rand()%size+1;
	  }

#ifdef DEBUG //unsorted arr
    for (int i = 0; i < size; i++) {
      printf("%d ",array[i]);
    }
    printf("\n");
#endif

omp_set_dynamic(0);     // Explicitly disable dynamic teams
omp_set_num_threads(numThreads); // Use a given number of threads for all consecutive parallel regions
start_time = omp_get_wtime();
    #pragma omp parallel  
    {
        #pragma omp single 
        {
            quickSort(array, 0, size-1);
        }
        numThreads = omp_get_num_threads();
    }
  /* get end time */
  end_time = omp_get_wtime();
  /* print results */
  printf("The execution time is %g sec\n", end_time - start_time);
  #ifdef DEBUG //sorted arr
  for (int i = 0; i < size; i++) {
    printf("%d ",array[i]);
  }
    printf("\n");  
    printf("tasks: %d\n", taskCount);
  #endif
  printf("numThreads: %d\n", numThreads);
}

