import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  user: User = new User();
  notLogged = true;
  isAdmin = false;
  isOwner = false;
  isDecorator = false;

  constructor() {}

  ngOnInit(): void {
    this.user = JSON.parse(localStorage.getItem('user') || '{}');
    if (this.user == null) {
      this.notLogged = true;
      this.isAdmin = false;
      this.isOwner = false;
      this.isDecorator = false;
    }
    if (this.user.userTypeId.userTypeId == 3) {
      this.notLogged = false;
      this.isAdmin = true;
      this.isOwner = false;
      this.isDecorator = false;
    }
    else if (this.user.userTypeId.userTypeId == 1) {
      this.notLogged = false;
      this.isAdmin = false;
      this.isOwner = true;
      this.isDecorator = false;
    }
    else if (this.user.userTypeId.userTypeId == 2) {
      this.notLogged = false;
      this.isAdmin = false;
      this.isDecorator = true;
      this.isOwner = false;
    }
    
  }

  logout() {
    localStorage.removeItem('user');
  }
}
