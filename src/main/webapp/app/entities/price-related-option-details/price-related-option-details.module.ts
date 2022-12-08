import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PriceRelatedOptionDetailsComponent } from './list/price-related-option-details.component';
import { PriceRelatedOptionDetailsDetailComponent } from './detail/price-related-option-details-detail.component';
import { PriceRelatedOptionDetailsUpdateComponent } from './update/price-related-option-details-update.component';
import { PriceRelatedOptionDetailsDeleteDialogComponent } from './delete/price-related-option-details-delete-dialog.component';
import { PriceRelatedOptionDetailsRoutingModule } from './route/price-related-option-details-routing.module';

@NgModule({
  imports: [SharedModule, PriceRelatedOptionDetailsRoutingModule],
  declarations: [
    PriceRelatedOptionDetailsComponent,
    PriceRelatedOptionDetailsDetailComponent,
    PriceRelatedOptionDetailsUpdateComponent,
    PriceRelatedOptionDetailsDeleteDialogComponent,
  ],
})
export class PriceRelatedOptionDetailsModule {}
