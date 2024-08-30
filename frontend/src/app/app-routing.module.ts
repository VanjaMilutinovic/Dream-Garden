import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent as GeneralLoginComponent } from './components/general/login/login.component';
import { LoginComponent as AdminLoginComponent } from './components/admin/login/login.component';
import { HomeComponent } from './components/general/home/home.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'login', component: GeneralLoginComponent },
  { path: 'admin/login', component: AdminLoginComponent },
  { path: '**', redirectTo: 'home' } // Wildcard route
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
