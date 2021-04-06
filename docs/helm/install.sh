#!/bin/bash
ns=${1:-hawk}

kubectl apply -f account.yaml

secretpodname=`kubectl -n kube-system get secret |grep hawk-token | head -n1 | awk '{print $1}'`
token=`kubectl -n kube-system get secret ${secretpodname} -o jsonpath={.data.token} | base64 -d`
apiserver=`kubectl config view --minify -o jsonpath='{.clusters[0].cluster.server}'`

helm install --create-namespace --namespace $ns --set hawk.kubernetes.token=$token --set hawk.kubernetes.apiServer=$apiserver hawk ./


echo "install istio deps..."

istions="istio-system"
kubectl create ns ${istions}

helm install --set global.jwtPolicy=first-party-jwt istio-base ./istio/base -n ${istions}
helm install --set global.jwtPolicy=first-party-jwt istiod ./istio/istio-control/istio-discovery -n ${istiod}
helm install --set global.jwtPolicy=first-party-jwt istio-ingress ./istio/gateways/istio-ingress -n ${istiod}
helm install --set global.jwtPolicy=first-party-jwt istio-egress ./istio/gateways/istio-egress -n ${istiod}

kubectl label namespace default istio-injection=enabled
