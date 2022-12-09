export interface IStyles {
  id: number;
  code?: string | null;
  description?: string | null;
  imgURL?: string | null;
  isActive?: boolean | null;
  width?: number | null;
  height?: number | null;
  x?: number | null;
  y?: number | null;
}

export type NewStyles = Omit<IStyles, 'id'> & { id: null };
