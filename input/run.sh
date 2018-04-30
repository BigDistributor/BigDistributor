#!/bin/sh
# This is my job script with qsub-options 
#$ -pe smp 8
##$ -pe orte 32
#$ -V -N "Java Task runner"
#$ -l h_rt=0:0:30 -l h_vmem=4G -l h_stack=128M -cwd
#$ -o data/test_results-$JOB_ID.txt
#$ -e data/test_results-$JOB_ID.txt
# export NSLOTS=8
# neccessary to prevent python error 
#export OPENBLAS_NUM_THREADS=4
# export NUM_THREADS=8
java -jar GaussianTask.jar 13.tif 12.tif 38.tif 10.tif 11.tif 39.tif 15.tif 29.tif 28.tif 14.tif 16.tif 17.tif 58.tif 64.tif 70.tif 71.tif 65.tif 59.tif 9.tif 67.tif 66.tif 72.tif 8.tif 62.tif 63.tif 61.tif 49.tif 48.tif 60.tif 45.tif 51.tif 3.tif 50.tif 2.tif 44.tif 52.tif 46.tif 47.tif 1.tif 53.tif 57.tif 5.tif 43.tif 42.tif 56.tif 4.tif 40.tif 6.tif 54.tif 68.tif 69.tif 7.tif 55.tif 41.tif 26.tif 32.tif 33.tif 27.tif 19.tif 31.tif 25.tif 24.tif 30.tif 18.tif 34.tif 20.tif 21.tif 35.tif 23.tif 37.tif 36.tif 22.tif
