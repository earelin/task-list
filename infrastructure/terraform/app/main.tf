terraform {
  required_version = ">= 1.11.0"
}

resource "kubernetes_namespace_v1" "app_namespace" {
  metadata {
    name = local.app_namespace_name
  }
}

module "app" {
  source      = "./modules/app"
  namespace   = local.app_namespace_name
  app_version = var.app_version
}

module "mongodb" {
  source    = "./modules/mongodb"
  namespace = local.app_namespace_name
}

resource "kubernetes_storage_class_v1" "local-storage-retain" {
  metadata {
    name = "local-storage-retain-class-${local.app_namespace_name}"
  }
  # reclaim_policy = "Retain"
  storage_provisioner = "k8s.io/minikube-hostpath"
}
