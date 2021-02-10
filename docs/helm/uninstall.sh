#!/bin/bash

ns=${1:-hawk}

helm uninstall ${ns} -n ${ns}

kubectl delete ns $ns
