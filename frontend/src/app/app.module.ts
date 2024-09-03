import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

import { LoginComponent as GeneralLoginComponent } from './components/general/login/login.component';
import { RegisterComponent } from './components/general/register/register.component';
import { ChangePasswordComponent } from './components/general/change-password/change-password.component';
import { HeaderComponent } from './components/util/header/header.component';
import { FooterComponent } from './components/util/footer/footer.component';
import { HomeComponent } from './components/general/home/home.component';
import { ProfileComponent } from './components/owner/profile/profile.component';
import { ProfileComponent as DecoratorProfileComponent } from './components/decorator/profile/profile.component';
import { CompaniesComponent as OwnerCompaniesComponent} from './components/owner/companies/companies.component';
import { JobComponent } from './components/owner/job/job.component';
import { JobComponent as DecoratorJobComponent } from './components/decorator/job/job.component';
import { MaintenanceComponent } from './components/owner/maintenance/maintenance.component';
import { StatisticComponent } from './components/decorator/statistic/statistic.component';
import { UsersComponent } from './components/admin/users/users.component';
import { CreateWorkerComponent } from './components/admin/create-worker/create-worker.component';
import { CompaniesComponent as AdminCompaniesComponent} from './components/admin/companies/companies.component';
import { CreateCompanyComponent } from './components/admin/create-company/create-company.component';
import { LeafletMapComponent } from './components/general/leaflet-map/leaflet-map.component';
import { GoogleMapsModule } from "@angular/google-maps";
import { NgxCaptchaModule } from 'ngx-captcha';
import { CompanyViewComponent } from './components/owner/company-view/company-view.component';
import { CanvasComponent } from './components/test/canvas/canvas.component';

import { DragDropModule } from '@angular/cdk/drag-drop';

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
    LeafletMapComponent,
    DecoratorJobComponent,
    CompanyViewComponent,
    DecoratorProfileComponent,
    CanvasComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatDatepickerModule,
    MatInputModule,
    MatNativeDateModule,
    MatFormFieldModule,
    NgxCaptchaModule,
    GoogleMapsModule,
    DragDropModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
