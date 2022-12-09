import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'customer',
        data: { pageTitle: 'Customers' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'books',
        data: { pageTitle: 'Books' },
        loadChildren: () => import('./books/books.module').then(m => m.BooksModule),
      },
      {
        path: 'selected-option',
        data: { pageTitle: 'SelectedOptions' },
        loadChildren: () => import('./selected-option/selected-option.module').then(m => m.SelectedOptionModule),
      },
      {
        path: 'selected-option-details',
        data: { pageTitle: 'SelectedOptionDetails' },
        loadChildren: () => import('./selected-option-details/selected-option-details.module').then(m => m.SelectedOptionDetailsModule),
      },
      {
        path: 'books-page',
        data: { pageTitle: 'BooksPages' },
        loadChildren: () => import('./books-page/books-page.module').then(m => m.BooksPageModule),
      },
      {
        path: 'page-layers',
        data: { pageTitle: 'PageLayers' },
        loadChildren: () => import('./page-layers/page-layers.module').then(m => m.PageLayersModule),
      },
      {
        path: 'page-layers-details',
        data: { pageTitle: 'PageLayersDetails' },
        loadChildren: () => import('./page-layers-details/page-layers-details.module').then(m => m.PageLayersDetailsModule),
      },
      {
        path: 'page-size',
        data: { pageTitle: 'PageSizes' },
        loadChildren: () => import('./page-size/page-size.module').then(m => m.PageSizeModule),
      },
      {
        path: 'books-option-details',
        data: { pageTitle: 'BooksOptionDetails' },
        loadChildren: () => import('./books-option-details/books-option-details.module').then(m => m.BooksOptionDetailsModule),
      },
      {
        path: 'books-attributes',
        data: { pageTitle: 'BooksAttributes' },
        loadChildren: () => import('./books-attributes/books-attributes.module').then(m => m.BooksAttributesModule),
      },
      {
        path: 'books-variables',
        data: { pageTitle: 'BooksVariables' },
        loadChildren: () => import('./books-variables/books-variables.module').then(m => m.BooksVariablesModule),
      },
      {
        path: 'price-related-option',
        data: { pageTitle: 'PriceRelatedOptions' },
        loadChildren: () => import('./price-related-option/price-related-option.module').then(m => m.PriceRelatedOptionModule),
      },
      {
        path: 'price-related-option-details',
        data: { pageTitle: 'PriceRelatedOptionDetails' },
        loadChildren: () =>
          import('./price-related-option-details/price-related-option-details.module').then(m => m.PriceRelatedOptionDetailsModule),
      },
      {
        path: 'books-related-option',
        data: { pageTitle: 'BooksRelatedOptions' },
        loadChildren: () => import('./books-related-option/books-related-option.module').then(m => m.BooksRelatedOptionModule),
      },
      {
        path: 'books-related-option-details',
        data: { pageTitle: 'BooksRelatedOptionDetails' },
        loadChildren: () =>
          import('./books-related-option-details/books-related-option-details.module').then(m => m.BooksRelatedOptionDetailsModule),
      },
      {
        path: 'option-type',
        data: { pageTitle: 'OptionTypes' },
        loadChildren: () => import('./option-type/option-type.module').then(m => m.OptionTypeModule),
      },
      {
        path: 'layer-group',
        data: { pageTitle: 'LayerGroups' },
        loadChildren: () => import('./layer-group/layer-group.module').then(m => m.LayerGroupModule),
      },
      {
        path: 'avatar-charactor',
        data: { pageTitle: 'AvatarCharactors' },
        loadChildren: () => import('./avatar-charactor/avatar-charactor.module').then(m => m.AvatarCharactorModule),
      },
      {
        path: 'options',
        data: { pageTitle: 'Options' },
        loadChildren: () => import('./options/options.module').then(m => m.OptionsModule),
      },
      {
        path: 'styles',
        data: { pageTitle: 'Styles' },
        loadChildren: () => import('./styles/styles.module').then(m => m.StylesModule),
      },
      {
        path: 'avatar-attributes',
        data: { pageTitle: 'AvatarAttributes' },
        loadChildren: () => import('./avatar-attributes/avatar-attributes.module').then(m => m.AvatarAttributesModule),
      },
      {
        path: 'layers',
        data: { pageTitle: 'Layers' },
        loadChildren: () => import('./layers/layers.module').then(m => m.LayersModule),
      },
      {
        path: 'layer-details',
        data: { pageTitle: 'LayerDetails' },
        loadChildren: () => import('./layer-details/layer-details.module').then(m => m.LayerDetailsModule),
      },
      {
        path: 'selections',
        data: { pageTitle: 'Selections' },
        loadChildren: () => import('./selections/selections.module').then(m => m.SelectionsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
