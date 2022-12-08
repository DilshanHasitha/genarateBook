export interface IPageSize {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
  width?: number | null;
  height?: number | null;
}

export type NewPageSize = Omit<IPageSize, 'id'> & { id: null };
