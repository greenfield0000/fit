#!/bin/bash

echo "undeploy frontend and service 4 this"

kubectl -f k8s.fit-b2b-front.service.yaml delete && kubectl -f k8s.fit-b2b-front.yaml delete
