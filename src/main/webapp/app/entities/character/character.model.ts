export interface ICharacter {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
}

export type NewCharacter = Omit<ICharacter, 'id'> & { id: null };
