import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent as GeneralLoginComponent } from './components/general/login/login.component';
import { RegisterComponent } from './components/general/register/register.component';
import { ChangePasswordComponent } from './components/general/change-password/change-password.component';
import { HeaderComponent } from './components/util/header/header.component';
import { FooterComponent } from './components/util/footer/footer.component';
import { HomeComponent } from './components/general/home/home.component';
import { ProfileComponent } from './components/owner/profile/profile.component';
import { CompaniesComponent as OwnerCompaniesComponent} from './components/owner/companies/companies.component';
import { JobComponent } from './components/owner/job/job.component';
import { MaintenanceComponent } from './components/owner/maintenance/maintenance.component';
import { StatisticComponent } from './components/decorator/statistic/statistic.component';
import { UsersComponent } from './components/admin/users/users.component';
import { CreateWorkerComponent } from './components/admin/create-worker/create-worker.component';
import { CompaniesComponent as AdminCompaniesComponent} from './components/admin/companies/companies.component';
import { CreateCompanyComponent } from './components/admin/create-company/create-company.component';
import { LeafletMapComponent } from './components/general/leaflet-map/leaflet-map.component';

@NgModule({
  declarations: [
    AppComponent,
    GeneralLoginComponent,
    RegisterComponent,
    ChangePasswordComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    ProfileComponent,
    OwnerCompaniesComponent,
    JobComponent,
    MaintenanceComponent,
    StatisticComponent,
    UsersComponent,
    CreateWorkerComponent,
    AdminCompaniesComponent,
    CreateCompanyComponent,
    LeafletMapComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
