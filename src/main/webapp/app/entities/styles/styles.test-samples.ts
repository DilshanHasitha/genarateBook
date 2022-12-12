import { IStyles, NewStyles } from './styles.model';

export const sampleWithRequiredData: IStyles = {
  id: 78144,
  description: 'program',
  isText: false,
};

export const sampleWithPartialData: IStyles = {
  id: 53645,
  description: 'content multi-byte Lead',
  imgURL: 'strategize blue generating',
  height: 73509,
  x: 83374,
  isText: true,
};

export const sampleWithFullData: IStyles = {
  id: 73834,
  code: 'blockchains Baby',
  description: 'Leu',
  imgURL: 'Optimization optical withdrawal',
  isActive: true,
  width: 45789,
  height: 57787,
  x: 93108,
  y: 25102,
  isText: false,
};

export const sampleWithNewData: NewStyles = {
  description: 'District copy',
  isText: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
