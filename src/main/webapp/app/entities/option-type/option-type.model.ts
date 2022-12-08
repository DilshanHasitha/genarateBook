export interface IOptionType {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
}

export type NewOptionType = Omit<IOptionType, 'id'> & { id: null };
