import { IBooks, NewBooks } from './books.model';

export const sampleWithRequiredData: IBooks = {
  id: 5514,
  name: 'Robust Zambian non-volatile',
  title: 'THX XML virtual',
  isActive: false,
};

export const sampleWithPartialData: IBooks = {
  id: 25811,
  name: 'Checking navigating Kansas',
  title: 'Interactions generate',
  subTitle: 'distributed target HTTP',
  author: 'Rapid Greenland',
  isActive: false,
  noOfPages: 42390,
  storeImg: 'ability',
};

export const sampleWithFullData: IBooks = {
  id: 56585,
  code: 'Estonia Monaco override',
  name: 'Unbranded Idaho',
  title: 'systems',
  subTitle: 'Fresh maroon generation',
  author: '9(E.U.A.-9)',
  isActive: false,
  noOfPages: 21544,
  storeImg: 'Lithuania Vatu',
};

export const sampleWithNewData: NewBooks = {
  name: 'Pound Leone Account',
  title: 'Account',
  isActive: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
