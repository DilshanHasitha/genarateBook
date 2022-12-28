import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FontFamilyComponent } from './list/font-family.component';
import { FontFamilyDetailComponent } from './detail/font-family-detail.component';
import { FontFamilyUpdateComponent } from './update/font-family-update.component';
import { FontFamilyDeleteDialogComponent } from './delete/font-family-delete-dialog.component';
import { FontFamilyRoutingModule } from './route/font-family-routing.module';

@NgModule({
  imports: [SharedModule, FontFamilyRoutingModule],
  declarations: [FontFamilyComponent, FontFamilyDetailComponent, FontFamilyUpdateComponent, FontFamilyDeleteDialogComponent],
})
export class FontFamilyModule {}
