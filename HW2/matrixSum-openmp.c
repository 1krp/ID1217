/* matrix summation using OpenMP

   usage with gcc (version 4.2 or higher required):
     gcc -O -fopenmp -o matrixSum-openmp matrixSum-openmp.c 
     ./matrixSum-openmp size numWorkers

*/

#include <omp.h>
#include <limits.h>
double start_time, end_time;
#include <stdlib.h>
#include <stdio.h>
#define MAXSIZE 10000  /* maximum matrix size */
#define MAXWORKERS 16   /* maximum number of workers */
#define MAXLOOP 10

int numWorkers;
int actualWorkers;
int size; 
int loop;
int matrix[MAXSIZE][MAXSIZE];


/* read command line, initialize, and create threads */
int main(int argc, char *argv[]) {
  omp_set_dynamic(0);     // Explicitly disable dynamic teams
  loop = (argc > 1)? atoi(argv[1]) : MAXLOOP;
  if (loop > MAXLOOP) loop = MAXLOOP;
  int matrixSize[] = {1000,2000,3000,4000,5000,6000,7000,8000,9000,10000};
  int matrixNumWorkers[] = {1,2,4,8,16,32}; 
  for (int l = 0; l < loop; l++) {
    for (int a = 0; a < 10; a++) {
      int size = matrixSize[a];
      #ifdef DEBUG
      size = matrixSize[a] / 1000;
      #endif
      for (int b = 0; b < 6; b++) {
        int numWorkers = matrixNumWorkers[b];

        int i, j;
        long total = 0;
        int matrixMin = INT_MAX;
        int matrixMax = -1;
        
        omp_set_num_threads(numWorkers);

          /* initialize the matrix */
          for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
              matrix[i][j] = rand()%(size*size);
            }
          }

        #ifdef DEBUG
          for (i = 0; i < size; i++) {
            printf("[");
            for (j = 0; j < size; j++) {
              printf(" %d", matrix[i][j]);
            }
            printf(" ]\n");
          }
        #endif

          start_time = omp_get_wtime();
        #pragma omp parallel for reduction(+:total) reduction(max:matrixMax) reduction(min:matrixMin) private(j) 
          for (i = 0; i < size; i++)
            for (j = 0; j < size; j++){
              int element = matrix[i][j];

              total += element;
              if(element < matrixMin){
                matrixMin = element;
              }
              if(element > matrixMax){
                matrixMax = element;
              }
            }
            actualWorkers = omp_get_num_threads();
        // implicit barrier
          end_time = omp_get_wtime();
          printf("size: %d, threads:%d, time: %g, tot = %lu, min = %d, max = %d \n", size, actualWorkers, end_time - start_time, total, matrixMin, matrixMax);
      }
      
    }


  }
 
}

