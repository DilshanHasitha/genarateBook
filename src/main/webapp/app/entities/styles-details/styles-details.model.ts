export interface IStylesDetails {
  id: number;
  isActive?: boolean | null;
  templateValue?: string | null;
  replaceValue?: string | null;
}

export type NewStylesDetails = Omit<IStylesDetails, 'id'> & { id: null };
