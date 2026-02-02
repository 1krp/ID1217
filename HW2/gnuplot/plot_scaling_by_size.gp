# ==========================================
# Strong scaling: Time (ms) vs Threads
# One averaged line per problem size
# ==========================================

set terminal pdfcairo enhanced font "Arial,11"
set output "time_vs_threads_by_size_ms.pdf"

set title "Execution Time vs Threads"
set xlabel "Number of Threads"
set ylabel "Time (milliseconds)"
set grid
set key outside right
set style data lines

# Start x-axis at 1 thread
set xrange [1:*]

# Problem sizes
sizes = "1000000 5000000 10000000"

plot for [s in sizes] \
"< cat quicksortData1m.txt quicksortData5m.txt quicksortData10m.txt \
| awk -F'[ ,]+' 'NF>=3 {sum[$1,$2]+=$3; cnt[$1,$2]++} \
END{for (k in sum){split(k,a,SUBSEP); print a[1], a[2], sum[k]/cnt[k]}}' \
| sort -k1,1n -k2,2n" \
using ( $1==int(s) ? $2 : 1/0 ) : ( $1==int(s) ? $3*1000.0 : 1/0 ) \
with lines lw 2 \
title sprintf("Size = %s", s)

unset output
