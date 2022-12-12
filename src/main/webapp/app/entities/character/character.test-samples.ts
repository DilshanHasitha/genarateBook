import { ICharacter, NewCharacter } from './character.model';

export const sampleWithRequiredData: ICharacter = {
  id: 9072,
};

export const sampleWithPartialData: ICharacter = {
  id: 42508,
  code: 'Mauritius Loan',
  isActive: true,
};

export const sampleWithFullData: ICharacter = {
  id: 6589,
  code: 'generate drive Sausages',
  description: 'toolset',
  isActive: true,
};

export const sampleWithNewData: NewCharacter = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
