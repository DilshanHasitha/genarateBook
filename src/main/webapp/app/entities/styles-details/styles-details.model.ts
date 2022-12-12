export interface IStylesDetails {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
}

export type NewStylesDetails = Omit<IStylesDetails, 'id'> & { id: null };
