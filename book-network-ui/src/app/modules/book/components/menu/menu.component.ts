import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TokenService } from '../../../../services/token/token.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { CommonModule } from '@angular/common';
import { SplitFullnamePipe } from '../../../../services/pipe/split-fullname.pipe';

@Component({
  selector: 'app-menu',
  imports: [RouterLink, CommonModule, SplitFullnamePipe],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {

  constructor(private tokenService:TokenService){}
  username: string = '';


  getUsername(): void{
    const jwtHelper = new JwtHelperService();
    const token = this.tokenService.token;
    const fullName = jwtHelper.decodeToken(token).fullName;
    this.username = fullName;
  }
  

  ngOnInit(): void {
    const linkColor = document.querySelectorAll('.nav-link');
    linkColor.forEach((link) => {
      if (window.location.href.endsWith(link.getAttribute('href') || '')) {
        link.classList.add('active');
      }
      link.addEventListener('click', () => {
        linkColor.forEach((l) => l.classList.remove('active'));
        link.classList.add('active');
      })
    })
    this.getUsername();
  }

  logout(): void {
    localStorage.removeItem('token');
    window.location.reload();
  }
}
