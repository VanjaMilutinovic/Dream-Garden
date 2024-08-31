import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent as GeneralLoginComponent } from './components/general/login/login.component';
import { HomeComponent } from './components/general/home/home.component';

import { ProfileComponent as OwnerProfileComponent} from './components/owner/profile/profile.component';
import { CompaniesComponent as OwnerCompaniesComponent } from './components/owner/companies/companies.component';
import { JobComponent as OwnerJobComponent } from './components/owner/job/job.component';
import { MaintenanceComponent as OwnerMaintenanceComponent } from './components/owner/maintenance/maintenance.component';

import { ProfileComponent as DecoratorProfileComponent } from './components/decorator/profile/profile.component';
import { JobComponent as DecoratorJobComponent } from './components/decorator/job/job.component';
import { MaintenanceComponent as DecoratorMaintenanceComponent } from './components/decorator/maintenance/maintenance.component';
import { StatisticComponent as DecoratorStatisticComponent } from './components/decorator/statistic/statistic.component';

import { LoginComponent as AdminLoginComponent } from './components/admin/login/login.component';
import { UsersComponent as AdminUsersComponent } from './components/admin/users/users.component';
import { CompaniesComponent as AdminCompaniesComponent } from './components/admin/companies/companies.component';
import { CreateWorkerComponent as AdminCreateWorkerComponent } from './components/admin/create-worker/create-worker.component';
import { CreateCompanyComponent as AdminCreateCompanyComponent } from './components/admin/create-company/create-company.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'login', component: GeneralLoginComponent },
  // OWNER
  { path: 'owner/profile', component: OwnerProfileComponent },
  { path: 'owner/companies', component: OwnerCompaniesComponent },
  { path: 'owne/job', component: OwnerJobComponent}, 
  { path: 'owner/maintenance', component: OwnerMaintenanceComponent},
  // DECORATOR
  { path: 'decorator/profile', component: DecoratorProfileComponent },
  { path: 'decorator/job', component: DecoratorJobComponent },
  { path: 'decorator/maintenance', component: DecoratorMaintenanceComponent },
  { path: 'decorator/statistic', component: DecoratorStatisticComponent },
  // ADMIN
  { path: 'admin/login', component: AdminLoginComponent },
  { path: 'admin/users', component: AdminUsersComponent },
  { path: 'admin/companies', component: AdminCompaniesComponent },
  { path: 'admin/users/createWorker', component: AdminCreateWorkerComponent },
  { path: 'admin/companies/createCompany', component: AdminCreateCompanyComponent },
  // Wildcard route
  { path: '**', redirectTo: 'home' } 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
