#!/bin/bash

ns=${1:-hawk}
kubectl get all --all-namespaces -l='app.kubernetes.io/managed-by=Helm'
helm status $ns -n $ns

host=`kubectl config view --minify -o jsonpath='{.clusters[0].cluster.server}'`
echo $host| sed -e 's|http[s]*://\(.*\):[0-9]*|\1|'

