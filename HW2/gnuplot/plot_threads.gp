# ==========================================
# Average Runtime vs Size (per thread)
# Time in microseconds (µs)
# ==========================================

rawfile   = "matrixData.txt"
cleanfile = "matrix_clean.dat"
avgfile   = "matrix_avg.dat"

# --- Step 1: Extract numeric columns (size threads time_s)
cmd1 = sprintf("awk -F'[,: ]+' '{print $2, $4, $6}' %s > %s", rawfile, cleanfile)
system(cmd1)

# --- Step 2: Average time per (size,thread) AND sort by thread then size
cmd2 = sprintf(\
"awk '{sum[$1,$2]+=$3; count[$1,$2]++} \
END{for (k in sum){split(k,a,SUBSEP); print a[1], a[2], sum[k]/count[k]}}' %s \
| sort -k2,2n -k1,1n > %s", cleanfile, avgfile)
system(cmd2)

# --- Plot ---
set terminal pdfcairo enhanced font "Arial,11"
set output "time_vs_size_threads_avg.pdf"

set title "Average Execution Time vs Problem Size - CPU has 16 threads"
set xlabel "Size"
set ylabel "Time (ms)"
set grid
set key outside right
set style data lines

threads = "1 2 4 8 16 32"

plot for [t in threads] avgfile using \
    ( $2==int(t) ? $1 : 1/0 ) : \
    ( $2==int(t) ? $3*1e3 : 1/0 ) \
    with lines lw 2 \
    title sprintf("%s Threads", t)

unset output
