#!/bin/bash

echo "deploy frontend and service 4 this"

kubectl -f k8s.fit-b2b-front.yaml apply && kubectl -f k8s.fit-b2b-front.service.yaml apply
