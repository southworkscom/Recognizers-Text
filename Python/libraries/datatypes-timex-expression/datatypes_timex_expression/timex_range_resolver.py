from datatypes_timex_expression import Timex, Constants, TimexHelpers
from datatypes_timex_expression.timex_constraints_helper import TimexConstraintsHelper


class TimexRangeResolver:
    def evaluate(self, candidates: [str], constraints:[str]):
        timex_constraints = list(map(lambda tc: Timex(tc), constraints))
        candidates_with_duration_resolved = self.resolve_duration(candidates, timex_constraints)
        candidates_according_to_date = self.resolve_by_date_range_constraints((candidates_with_duration_resolved, timex_constraints))
        candidates_with_added_time = self.resolve_by_time_constraints(candidates_according_to_date, timex_constraints)
        candidates_filtered_by_time = self.resolve_by_date_range_constraints(candidates_with_added_time, timex_constraints)

        timex_results = list(map(lambda tc: Timex(tc), candidates_filtered_by_time))

        return timex_results

    def resolve_durations(self, candidates: [str], constraints: [Timex]):
        results = []
        for candidate in candidates:
            timex = Timex(candidate)

            if Constants.TIMEX_TYPES_DURATION in timex.types():
                resolved_duration = self.resolve_duration(timex,constraints)

                for resolved in resolved_duration:
                    results.append(resolved.timex_value())
            else:
                results.append(candidate)
        return results

    @staticmethod
    def resolve_duration(candidate: Timex, constraints: [Timex]):
        results = []

        for constraint in constraints:
            if Constants.TIMEX_TYPES_DATETIME in constraint.types():
                results.append(TimexHelpers.timex_datetime_add(constraint, candidate))
            elif Constants.TIMEX_TYPES_TIME in constraint.types():
                results.append(TimexHelpers.timex_time_add(constraint, candidate))

        return results

    def resolve_by_date_range_constraints(self, candidates: [str], timex_constraints:[Timex]):
        date_range_constraints = map(lambda t: TimexHelpers.daterange_from_timex(t),
                                     filter(lambda t: Constants.TIMEX_TYPES_DATERANGE in t.types(), timex_constraints))

        collapsed_date_ranges = TimexConstraintsHelper.collapse(date_range_constraints)

        if not any(collapsed_date_ranges):
            return candidates

        resolution = []

        for timex in candidates:
            resolve_date = self.resolve_date(Timex(timex), collapsed_date_ranges)
            resolution.extend(resolve_date)

        return self.remove_duplicates(resolution)

    def resolve_date(self, timex: Timex, constraints):
        result = []

        for constraint in constraints:
            result.extend(self.resolve_date_against_constraint(timex, constraint))

        return result

    def resolve_by_timerange_constraints(self, candidates, timex_constraints):
        pass

    def resolve_timerage(self,timex: Timex, constraints):
        pass

    def remove_duplicates(self, original):
        pass

    def resolve_definite_against_constraint(self, timex, constraint):
        pass

    def resolve_date_against_constraint(self, timex, constraint):
        pass

    def resolve_by_time_constraints(self, candidates, timex_constraints):
        pass


