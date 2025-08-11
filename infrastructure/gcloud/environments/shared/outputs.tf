output "network_name" {
  value = module.network.network_name
}

output "subnetwork_name" {
  value = module.network.subnetwork_name
}

output "github_actions_service_account_email" {
  value = module.github_actions.service_account_email
}
