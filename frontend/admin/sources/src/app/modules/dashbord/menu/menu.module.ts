import { FullCalendarModule } from '@fullcalendar/angular';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenuRoutingModule } from './menu-routing.module';
import { DashbordComponent } from '../dashbord.component';
import { PersonalModule } from './personal/personal.module';
import { ScheduleComponent } from './schedule/schedule.component';

@NgModule({
  declarations: [
    // menu (menu pages) component
    DashbordComponent,
    ScheduleComponent
  ],
  imports: [
    CommonModule,
    MenuRoutingModule,
    RouterModule,
    // JournalsModule
    PersonalModule,
    FullCalendarModule
  ]
})
export class MenuModule { }
