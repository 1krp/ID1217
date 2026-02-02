# ==========================================
# Normalized strong scaling
# X: Threads
# Y: Normalized time (T(p) / T(1))  => all curves start at 1.0
# One averaged line per problem size
# ==========================================

set terminal pdfcairo enhanced font "Arial,11"
set output "time_vs_threads_normalized.pdf"

set title "Normalized Execution Time vs Threads"
set xlabel "Number of Threads"
set ylabel "Normalized time  (T(p) / T(1))"
set grid
set key outside right
set style data lines

# Problem sizes (one curve per size)
sizes = "1000000 5000000 10000000"

# Precompute averaged times in awk, then normalize using the per-size baseline at p=1.
# Output columns: size threads normalized_time
data = "< cat quicksortData1m.txt quicksortData5m.txt quicksortData10m.txt \
| awk -F'[ ,]+' 'NF>=3 {sum[$1,$2]+=$3; cnt[$1,$2]++} \
END{ \
  for (k in sum){ \
    split(k,a,SUBSEP); \
    avg[a[1],a[2]] = sum[k]/cnt[k]; \
    if (a[2]==1) base[a[1]] = avg[a[1],a[2]]; \
  } \
  for (k in avg){ \
    split(k,a,SUBSEP); \
    if (base[a[1]]>0) print a[1], a[2], avg[a[1],a[2]]/base[a[1]]; \
  } \
}' \
| sort -k1,1n -k2,2n"

plot for [s in sizes] data using \
    ( $1==int(s) ? $2 : 1/0 ) : \
    ( $1==int(s) ? $3 : 1/0 ) \
    with lines lw 2 \
    title sprintf("N = %s", s)

unset output
