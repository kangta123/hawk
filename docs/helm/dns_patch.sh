#!/bin/bash

host=${1}
if [ -z "${host}" ]; then
  echo "domain host canot be null. eg: sh dns.sh hawk.cn"
  exit
fi


kubectl patch -n kube-system svc kube-dns --patch "$(cat coredns_nodeport_patch.yaml)"

apiserver=`kubectl config view --minify -o jsonpath='{.clusters[0].cluster.server}'|sed -e 's|http[s]*://\(.*\):[0-9]*|\1|'`

echo "apiserver ip : $apiserver"

tmp=$(mktemp dnsconfig.XXXXXX)
cat << EOF > $tmp
data:
  Corefile: |
    .:53 {
        errors
        health
        ready
        hosts {
            $apiserver $host
            fallthrough
        }
        kubernetes cluster.local in-addr.arpa ip6.arpa {
           pods insecure
           fallthrough in-addr.arpa ip6.arpa
           ttl 30
        }
        template IN A $host {
          match .*\.${host//\./\\.}
          answer "{{ .Name }} 60 IN A $apiserver"
          fallthrough
        }
        prometheus :9153
        forward . /etc/resolv.conf
        cache 30
        loop
        reload
        loadbalance
    }
EOF

cat $tmp
kubectl patch -n kube-system cm coredns --patch "$(cat $tmp)"


rm -fr dnsconfig.*

