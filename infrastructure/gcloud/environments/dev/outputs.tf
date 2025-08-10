output "github_actions_service_account_email" {
  description = "Service account email used by GitHub Actions for WIF"
  value       = module.github_actions.service_account_email
}

output "github_actions_workload_identity_provider" {
  description = "Full resource name of the Workload Identity Provider for GitHub Actions"
  value       = module.github_actions.workload_identity_provider
}
