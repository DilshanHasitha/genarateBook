import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PriceRelatedOptionComponent } from './list/price-related-option.component';
import { PriceRelatedOptionDetailComponent } from './detail/price-related-option-detail.component';
import { PriceRelatedOptionUpdateComponent } from './update/price-related-option-update.component';
import { PriceRelatedOptionDeleteDialogComponent } from './delete/price-related-option-delete-dialog.component';
import { PriceRelatedOptionRoutingModule } from './route/price-related-option-routing.module';

@NgModule({
  imports: [SharedModule, PriceRelatedOptionRoutingModule],
  declarations: [
    PriceRelatedOptionComponent,
    PriceRelatedOptionDetailComponent,
    PriceRelatedOptionUpdateComponent,
    PriceRelatedOptionDeleteDialogComponent,
  ],
})
export class PriceRelatedOptionModule {}
