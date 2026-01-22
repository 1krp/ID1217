/* matrix summation using pthreads

   features: uses a barrier; the Worker[0] computes
             the total sum from partial sums computed by Workers
             and prints the total sum to the standard output

   usage under Linux:
     gcc matrixSum.c -lpthread
     a.out size numWorkers

*/
#ifndef _REENTRANT 
#define _REENTRANT 
#endif 
#include <pthread.h>
#include <limits.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <time.h>
#include <sys/time.h>
#define MAXSIZE 10000  /* maximum matrix size */
#define MAXWORKERS 10   /* maximum number of workers */
int numWorkers;           /* number of workers */ 
int numArrived = 0;       /* number who have arrived */

/* timer */
double read_timer() {
    static bool initialized = false;
    static struct timeval start;
    struct timeval end;
    if( !initialized )
    {
        gettimeofday( &start, NULL );
        initialized = true;
    }
    gettimeofday( &end, NULL );
    return (end.tv_sec - start.tv_sec) + 1.0e-6 * (end.tv_usec - start.tv_usec);
}
  struct pointValue {
    int value;
    int x;
    int y;
  }; 
 
double start_time, end_time; /* start and end times */
int size, stripSize;  /* assume size is multiple of numWorkers */
int sums[MAXWORKERS]; /* partial sums */

/*  sharded variable  */
int sharedSum;
pthread_mutex_t sharedtotal_mutex;

struct pointValue sharedMax;
pthread_mutex_t sharedmax_mutex;

struct pointValue sharedMin;
pthread_mutex_t sharedmin_mutex;

int matrix[MAXSIZE][MAXSIZE]; /* matrix */

void *Worker(void *);

/* read command line, initialize, and create threads */
int main(int argc, char *argv[]) {
  int i, j;
  long l; /* use long in case of a 64-bit system */
  pthread_attr_t attr;
  pthread_t workerid[MAXWORKERS];

  /* set global thread attributes */
  pthread_attr_init(&attr);
  pthread_attr_setscope(&attr, PTHREAD_SCOPE_SYSTEM);

  /* initialize mutex*/

  pthread_mutex_init(&sharedtotal_mutex, NULL);
  pthread_mutex_init(&sharedmax_mutex, NULL);
  pthread_mutex_init(&sharedmin_mutex, NULL);

  /* read command line args if any */
  size = (argc > 1)? atoi(argv[1]) : MAXSIZE;
  numWorkers = (argc > 2)? atoi(argv[2]) : MAXWORKERS;
  if (size > MAXSIZE) size = MAXSIZE;
  if (numWorkers > MAXWORKERS) numWorkers = MAXWORKERS;
  stripSize = size/numWorkers;

  /* initialize the matrix */
  for (i = 0; i < size; i++) {
	  for (j = 0; j < size; j++) {
          matrix[i][j] = rand()%99; //1
	  }
  }

  sharedMin.value = INT_MAX;

  /* print the matrix */
#ifdef DEBUG
  for (i = 0; i < size; i++) {
	  printf("[ ");
	  for (j = 0; j < size; j++) {
	    printf(" %d", matrix[i][j]);
	  }
	  printf(" ]\n");
  }
#endif

  /* do the parallel work: create the workers */
  start_time = read_timer();
  for (l = 0; l < numWorkers; l++)
    pthread_create(&workerid[l], &attr, Worker, (void *) l);

  for (l = 0; l < numWorkers; l++) {
    pthread_join(workerid[l], NULL);
  }

  /* get end time */
  end_time = read_timer();
  /* print results */
  printf("The total is %d\n", sharedSum);
  printf("Max found at  x: %d y: %d value: %d\n", sharedMax.x, sharedMax.y, sharedMax.value);
  printf("Min found at  x: %d y: %d value: %d\n", sharedMin.x, sharedMin.y, sharedMin.value);
  printf("The execution time is %g sec\n", end_time - start_time);

  
  pthread_exit(NULL);
}

/* Each worker sums the values in one strip of the matrix.
   After a barrier, worker(0) computes and prints the total */
void *Worker(void *arg) {
  long myid = (long) arg;
  int total, i, j, first, last;

#ifdef DEBUG
  printf("worker %d (pthread id %d) has started\n", myid, pthread_self());
#endif

  /* determine first and last rows of my strip */
  first = myid*stripSize;
  last = (myid == numWorkers - 1) ? (size - 1) : (first + stripSize - 1);

  /* sum values in my strip */
  total = 0;
  struct pointValue max;
  struct pointValue min;
  max.value = -1;
  max.x = -1;
  max.y = -1;
  min.x = -1;
  min.y = -1; 
  min.value = 100;
  for (i = first; i <= last; i++){
    for (j = 0; j < size; j++){
      total += matrix[i][j];
      if(max.value<matrix[i][j]){ max.value = matrix[i][j]; max.x = i; max.y = j; }
      if(min.value>matrix[i][j]){ min.value = matrix[i][j]; min.x = i; min.y = j; }
    }
  }
  pthread_mutex_lock(&sharedtotal_mutex);
  sharedSum += total;
  pthread_mutex_unlock(&sharedtotal_mutex);

  pthread_mutex_lock(&sharedmax_mutex);
  if (sharedMax.value < max.value) { 
    sharedMax = max;
  }
  pthread_mutex_unlock(&sharedmax_mutex);

  pthread_mutex_lock(&sharedmin_mutex);
  if (sharedMin.value > min.value) {
    sharedMin = min;
  }
  pthread_mutex_unlock(&sharedmin_mutex);

}
