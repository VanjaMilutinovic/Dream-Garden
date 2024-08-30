import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent as GeneralLoginComponent } from './components/general/login/login.component';
import { RegisterComponent } from './components/general/register/register.component';
import { ChangePasswordComponent } from './components/general/change-password/change-password.component';
import { HeaderComponent } from './components/util/header/header.component';
import { FooterComponent } from './components/util/footer/footer.component';
import { HomeComponent } from './components/general/home/home.component';
import { LoginComponent as AdminLoginComponent } from './components/admin/login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    GeneralLoginComponent,
    AdminLoginComponent,
    RegisterComponent,
    ChangePasswordComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
