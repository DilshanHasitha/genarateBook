import { IPageLayers, NewPageLayers } from './page-layers.model';

export const sampleWithRequiredData: IPageLayers = {
  id: 95721,
  layerNo: 74767,
};

export const sampleWithPartialData: IPageLayers = {
  id: 1374,
  layerNo: 48295,
};

export const sampleWithFullData: IPageLayers = {
  id: 68992,
  layerNo: 12183,
  isActive: true,
};

export const sampleWithNewData: NewPageLayers = {
  layerNo: 57044,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
