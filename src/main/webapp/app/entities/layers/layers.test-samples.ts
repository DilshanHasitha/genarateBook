import { ILayers, NewLayers } from './layers.model';

export const sampleWithRequiredData: ILayers = {
  id: 78673,
  layerNo: 88091,
};

export const sampleWithPartialData: ILayers = {
  id: 20530,
  layerNo: 7736,
};

export const sampleWithFullData: ILayers = {
  id: 84111,
  layerNo: 63624,
  isActive: false,
};

export const sampleWithNewData: NewLayers = {
  layerNo: 85239,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
