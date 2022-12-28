import { IImageStoreType, NewImageStoreType } from './image-store-type.model';

export const sampleWithRequiredData: IImageStoreType = {
  id: 20969,
  imageStoreTypeCode: 'models Cross-platform',
  imageStoreTypeDescription: 'Intranet content-based',
};

export const sampleWithPartialData: IImageStoreType = {
  id: 5592,
  imageStoreTypeCode: 'Soap French',
  imageStoreTypeDescription: 'tan Card',
  isActive: false,
};

export const sampleWithFullData: IImageStoreType = {
  id: 13930,
  imageStoreTypeCode: 'Tuvalu optimal Associate',
  imageStoreTypeDescription: 'indexing turquoise',
  isActive: true,
};

export const sampleWithNewData: NewImageStoreType = {
  imageStoreTypeCode: 'optical ivory',
  imageStoreTypeDescription: 'Implemented',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
