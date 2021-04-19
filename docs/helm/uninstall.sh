#!/bin/bash

ns=${1:-hawk}

helm uninstall ${ns} -n ${ns}

kubectl delete ns $ns

helm uninstall istio-base -n ${ns}
helm uninstall istiod -n ${ns}
helm uninstall istio-ingress -n ${ns}
helm uninstall istio-egress -n ${ns}


kubectl delete ns istio-system
kubectl  delete sa hawk -n kube-system 
