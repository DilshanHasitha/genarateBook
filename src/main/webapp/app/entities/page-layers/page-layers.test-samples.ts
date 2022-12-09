import { IPageLayers, NewPageLayers } from './page-layers.model';

export const sampleWithRequiredData: IPageLayers = {
  id: 95721,
  layerNo: 74767,
};

export const sampleWithPartialData: IPageLayers = {
  id: 68992,
  layerNo: 12183,
};

export const sampleWithFullData: IPageLayers = {
  id: 56816,
  layerNo: 57044,
  isActive: true,
  isEditable: false,
  isText: true,
};

export const sampleWithNewData: NewPageLayers = {
  layerNo: 81913,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
