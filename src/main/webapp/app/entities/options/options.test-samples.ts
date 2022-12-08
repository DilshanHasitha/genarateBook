import { IOptions, NewOptions } from './options.model';

export const sampleWithRequiredData: IOptions = {
  id: 23828,
  description: 'Soft Future-proofed',
};

export const sampleWithPartialData: IOptions = {
  id: 4836,
  code: 'streamline program',
  description: 'Tuna Gorgeous Fresh',
};

export const sampleWithFullData: IOptions = {
  id: 95359,
  code: 'Leone up',
  description: 'best-of-breed TCP Handcrafted',
  imgURL: 'calculate',
  isActive: false,
};

export const sampleWithNewData: NewOptions = {
  description: 'disintermediate',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
