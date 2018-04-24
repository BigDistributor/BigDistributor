#!/bin/sh
# This is my job script with qsub-options 
##$ -pe smp 8
##$ -pe orte 32
#$ -V -N "Java Task runner"
#$ -l h_rt=0:0:30 -l h_vmem=4G -l h_stack=128M -cwd
#$ -o data/test_results-$JOB_ID.txt
#$ -e data/test_results-$JOB_ID.txt
# export NSLOTS=8
# neccessary to prevent python error 
export OPENBLAS_NUM_THREADS=4
# export NUM_THREADS=8
java -jar GaussianTask.jar DrosophilaWing.tif
