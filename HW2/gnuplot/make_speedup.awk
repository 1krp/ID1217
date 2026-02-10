# make_speedup.awk
# Input: N, threads, time  (comma-separated, optional spaces)
# Output: "threads speedup" to stdout

BEGIN {
  FS=","            # comma-separated
}

{
  # trim leading/trailing whitespace
  gsub(/^[ \t]+|[ \t]+$/, "", $2)
  gsub(/^[ \t]+|[ \t]+$/, "", $3)

  t = $2 + 0
  time = $3 + 0

  sum[t] += time
  cnt[t] += 1
}

END {
  # thread counts we care about (edit if needed)
  n = split("1 2 4 8 16", T, " ")

  # compute averages
  for (i=1; i<=n; i++) {
    t = T[i]
    avg[t] = sum[t] / cnt[t]
  }

  T1 = avg[1]

  # output threads speedup
  for (i=1; i<=n; i++) {
    t = T[i]
    printf "%d %.10f\n", t, (T1 / avg[t])
  }
}
