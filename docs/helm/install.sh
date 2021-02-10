#!/bin/bash

ns=${1:-hawk}

kubectl apply -f account.yaml

secretpodname=`kubectl -n kube-system get secret |grep hawk-token | head -n1 | awk '{print $1}'`
token=`kubectl -n kube-system get secret ${secretpodname} -o jsonpath={.data.token} | base64 -d`
apiserver=`kubectl config view --minify -o jsonpath='{.clusters[0].cluster.server}'`

helm install --create-namespace --namespace $ns --set hawk.kubernetes.token=$token --set hawk.kubernetes.apiServer=$apiserver hawk ./ 

