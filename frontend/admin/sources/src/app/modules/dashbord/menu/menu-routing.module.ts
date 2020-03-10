import { DashbordComponent } from '../dashbord.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ScheduleComponent } from './schedule/schedule.component';
import { PersonalModule } from './personal/personal.module';

const routes: Routes = [
  {
    path: '', component: DashbordComponent
  },
  { path: 'personal', loadChildren: () => PersonalModule },
  { path: 'schedule', component: ScheduleComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MenuRoutingModule { }
