import { IImages, NewImages } from './images.model';

export const sampleWithRequiredData: IImages = {
  id: 22223,
};

export const sampleWithPartialData: IImages = {
  id: 48720,
  imageBlob: '../fake-data/blob/hipster.png',
  imageBlobContentType: 'unknown',
  originalURL: 'Moldova Manors',
};

export const sampleWithFullData: IImages = {
  id: 85694,
  imageBlob: '../fake-data/blob/hipster.png',
  imageBlobContentType: 'unknown',
  imageURL: 'index Trail dynamic',
  imageName: 'Berkshire',
  lowResURL: 'IB Wooden',
  originalURL: 'Technician magenta Cambridgeshire',
};

export const sampleWithNewData: NewImages = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
