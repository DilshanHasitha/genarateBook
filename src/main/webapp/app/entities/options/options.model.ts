export interface IOptions {
  id: number;
  code?: string | null;
  description?: string | null;
  imgURL?: string | null;
  isActive?: boolean | null;
}

export type NewOptions = Omit<IOptions, 'id'> & { id: null };
