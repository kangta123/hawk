#!/bin/bash

ns=${1:-hawk}

helm uninstall ${ns} -n ${ns}

kubectl delete ns $ns

helm uninstall istio-base -n ${ns}
helm uninstall istiod -n ${ns}
helm uninstall istio-ingress -n ${ns}
helm uninstall istio-egress -n ${ns}


kubectl delete ns istio-system
kubectl delete envoyfilters.networking.istio.io --all
kubectl delete sa hawk -n kube-system

kubectl delete clusterrole istiod-istio-system
kubectl delete clusterrole istio-reader-istio-system
kubectl delete  mutatingwebhookconfigurations.admissionregistration.k8s.io istio-sidecar-injector

kubectl delete clusterrolebindings.rbac.authorization.k8s.io istio-reader-istio-system
kubectl delete clusterrolebindings.rbac.authorization.k8s.io istiod-istio-system
kubectl delete validatingwebhookconfigurations.admissionregistration.k8s.io istiod-istio-system
