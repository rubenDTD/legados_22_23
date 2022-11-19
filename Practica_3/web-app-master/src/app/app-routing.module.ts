import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ZxSpectrumComponent } from './components/zx-spectrum/zx-spectrum.component';

const routes: Routes = [
  {path: '**', component: ZxSpectrumComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
