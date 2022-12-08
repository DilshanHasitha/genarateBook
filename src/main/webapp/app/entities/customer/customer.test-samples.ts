import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 24379,
  name: 'Operations',
  phone: '968.502.8755',
  city: 'Turcotteport',
};

export const sampleWithPartialData: ICustomer = {
  id: 36162,
  code: 'e-business',
  name: 'Estonia input withdrawal',
  creditLimit: 50737,
  phone: '(635) 862-5720',
  addressLine2: 'withdrawal Maine',
  city: 'Corbinshire',
  country: 'Syrian Arab Republic',
  email: 'Reilly7@hotmail.com',
};

export const sampleWithFullData: ICustomer = {
  id: 33696,
  code: 'Angola',
  name: 'Analyst calculate up',
  creditLimit: 71694,
  rating: 99519,
  isActive: true,
  phone: '1-713-239-8115 x01796',
  addressLine1: 'Multi-channelled',
  addressLine2: 'Centralized',
  city: 'Jakaylaview',
  country: 'Barbados',
  email: 'Bernhard_Sanford97@gmail.com',
};

export const sampleWithNewData: NewCustomer = {
  name: 'framework',
  phone: '(306) 670-7087 x094',
  city: 'Berniershire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
